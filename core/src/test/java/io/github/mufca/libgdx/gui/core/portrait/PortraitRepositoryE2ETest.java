package io.github.mufca.libgdx.gui.core.portrait;

import static java.awt.Color.WHITE;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;

import com.badlogic.gdx.files.FileHandle;
import io.github.mufca.libgdx.datastructure.lowlevel.IdProvider;
import io.github.mufca.libgdx.util.LogHelper;
import io.github.mufca.libgdx.utils.GdxExtension;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
public class PortraitRepositoryE2ETest {

    @RegisterExtension
    private static final GdxExtension extension = new GdxExtension();

    private static final String STARTING_PORTRAIT_REGISTRATION = "Starting portrait registration at %d";
    private static final String TEST_DIRECTORY = "build/tmp/testFiles";
    private static final String FILEPATH_PATTERN = TEST_DIRECTORY + "/%s/%s";
    private static final String INFO_FILES_CREATED = "Successfully created %d files";
    private static final String DELETED_TEST_DIRECTORY = "Deleted test directory: %s";
    private static final String ATTEMPTING_TO_LOAD_PORTRAIT_FILES = "Attempting to load %d portrait files from entry parameter set";
    private static final String CLEANUP_FAILED = "Cleanup failed: ";
    private static final String PNG_FILE_FORMAT = "png";
    private static final String SUMMARY = "Ended all portrait registration at %d, running time %.5f s";
    private static final int MAX_CACHE_SIZE = 70;

    private final IdProvider idProvider = new IdProvider();

    private Set<GeneratedPortrait> setupFiles(Set<PortraitGenerationParameter> files) {
        LogHelper.info(this, ATTEMPTING_TO_LOAD_PORTRAIT_FILES.formatted(files.size()));
        var toReturn = files.stream().map(this::generateRandomPortrait).collect(Collectors.toSet());
        LogHelper.info(this, INFO_FILES_CREATED.formatted(toReturn.size()));
        return toReturn;
    }

    @AfterEach
    public void cleanupFilesAndExit() {
        var testDirectory = new File(TEST_DIRECTORY);
        try {
            FileUtils.deleteDirectory(testDirectory);
            LogHelper.info(this, DELETED_TEST_DIRECTORY.formatted(testDirectory.getAbsolutePath()));
        } catch (IOException ioException) {
            LogHelper.error(this, CLEANUP_FAILED + ioException.getMessage());
            fail(CLEANUP_FAILED, ioException);
        }
    }

    @ParameterizedTest
    @MethodSource("portraitTestData")
    public void shouldRegisterAndDisplayPortrait(Set<PortraitGenerationParameter> parameters) {
        // GIVEN
        PortraitRepository repository = new PortraitRepository(idProvider);
        var portraits = CompletableFuture.supplyAsync(() -> setupFiles(parameters)).join();

        // WHEN
        portraits.forEach(portrait -> this.registerPortraitAsync(portrait, repository));
        extension.setRenderCallback((panel, delta)
            -> PortraitRepositoryE2ETestRenderer.render(panel, repository));

        // THEN
        int expectedSize = Math.min(parameters.size(), MAX_CACHE_SIZE);
        await()
            .atMost(Duration.ofSeconds(2))
            .pollInterval(Duration.ofMillis(50))
            .until(() -> repository.snapshotPortraits().size() == expectedSize);
    }

    @ParameterizedTest
    @MethodSource("portraitStressData")
    public void shouldRegisterAndDisplayPortraitsUnderStress(Set<PortraitGenerationParameter> parameters)
        throws InterruptedException {

        // GIVEN
        PortraitRepository repository = new PortraitRepository(idProvider);
        var portraits = setupFiles(parameters);

        // WHEN
        var start = System.nanoTime();
        LogHelper.info(this, STARTING_PORTRAIT_REGISTRATION.formatted(start));
        List<CompletableFuture<Void>> futures = portraits.stream()
            .map(portrait -> repository.registerPortraitAsync(portrait.characterId(), new FileHandle(portrait.file()),
                portrait.type()))
            .toList();
        extension.setRenderCallback((panel, delta)
            -> PortraitRepositoryE2ETestRenderer.renderUnderStress(panel, repository));

        // THEN
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofMillis(50))
            .until(allTasks::isDone);
        var end = System.nanoTime();
        var running = end - start;
        double seconds = running / 1_000_000_000.0;
        LogHelper.info(this, SUMMARY.formatted(end, seconds));
        Thread.sleep(3000);
    }

    private GeneratedPortrait generateRandomPortrait(PortraitGenerationParameter parameter) {
        int width = parameter.type().width();
        int height = parameter.type().height();
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();
        var rand = new Random();
        var path = FILEPATH_PATTERN.formatted(parameter.directory(), parameter.type().filename());

        try {
            // Background
            var baseColor = new Color(
                rand.nextInt(256),
                rand.nextInt(256),
                rand.nextInt(256));
            graphics.setColor(baseColor);
            graphics.fillRect(0, 0, width, height);

            // Random noise
            for (int i = 0; i < 1000; i++) {
                graphics.setColor(new Color(
                    rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256)));
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                int rectangleWidth = rand.nextInt(20) + 1;
                int rectangleHeight = rand.nextInt(20) + 1;
                graphics.fillRect(x, y, rectangleWidth, rectangleHeight);
            }

            // Text note
            graphics.setColor(WHITE);
            graphics.drawString(path, 10, 20);
        } finally {
            graphics.dispose();
        }

        File tempFile = new File(path);
        tempFile.getParentFile().mkdirs();
        try {
            ImageIO.write(image, PNG_FILE_FORMAT, tempFile);
        } catch (IOException e) {
            LogHelper.error(this, e.getMessage());
            fail(e.getMessage());
        }
        return new GeneratedPortrait(parameter.characterId(), tempFile, parameter.type());
    }

    private void registerPortraitAsync(GeneratedPortrait portrait, PortraitRepository repository) {
        repository.registerPortraitAsync(portrait.characterId(), new FileHandle(portrait.file()), portrait.type());
    }

    private static Stream<Arguments> portraitStressData() {
        return new PortraitTestData().getStressData();
    }

    private static Stream<Arguments> portraitTestData() {
        return new PortraitTestData().getPortraits();
    }
}
