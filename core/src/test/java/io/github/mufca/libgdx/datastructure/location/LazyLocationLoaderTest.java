package io.github.mufca.libgdx.datastructure.location;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class LazyLocationLoaderTest {

    private static final String LOCATIONS_FOREST_GLADE_TEST = "locations/forest_glade_test";
    private static final String MISSING_FILE = "forest_glade_001.json";
    private static final String INVALID_FILENAME = "forest_glade_001";
    private static final String FOREST_GLADE_0001 = "forest_glade_0001";
    private static final String FOREST_GLADE_0002 = "forest_glade_0002";
    private static final String FOREST_GLADE_0003 = "forest_glade_0003";
    private static final String FOREST_GLADE_0004 = "forest_glade_0004";

    @BeforeEach
    public void mockGdxFiles() {
        // GIVEN
        Gdx.files = Mockito.mock(Files.class);
        when(Gdx.files.internal(anyString()))
            .thenAnswer(inv -> new FileHandle("src/test/resources/" + inv.getArgument(0)));
    }

    public static Stream<Arguments> locationProvider() {
        return Stream.of(
            Arguments.of(FOREST_GLADE_0001, "Shady forest glade",
                "A quiet glade surrounded by tall oaks. The ground is covered with moss, and you hear the distant sound" +
                " of running water.",
                List.of(
                    new Exit("east", FOREST_GLADE_0002),
                    new Exit("south-east", FOREST_GLADE_0004),
                    new Exit("south", FOREST_GLADE_0003)
                )),
            Arguments.of(FOREST_GLADE_0002, "Glade with a fallen tree",
                "This glade is marked by a massive fallen oak lying across the clearing. Birds nest among its broken " +
                "branches.",
                List.of(
                    new Exit("west", FOREST_GLADE_0001),
                    new Exit("south-west", FOREST_GLADE_0003),
                    new Exit("south", FOREST_GLADE_0004)
                )),
            Arguments.of(FOREST_GLADE_0003, "Glade with a stone circle",
                "Several mossy stones are arranged in a rough circle here. The air feels colder, as if the place held" +
                " ancient secrets.",
                List.of(
                    new Exit("north", FOREST_GLADE_0001),
                    new Exit("north-east", FOREST_GLADE_0002),
                    new Exit("east", FOREST_GLADE_0004)
                )),
            Arguments.of(FOREST_GLADE_0004, "Sunny forest glade",
                "Sunlight pierces through the canopy above, illuminating wildflowers in vivid colors. The buzzing of " +
                "bees fills the clearing.",
                List.of(
                    new Exit("north-west", FOREST_GLADE_0001),
                    new Exit("north", FOREST_GLADE_0002),
                    new Exit("west", FOREST_GLADE_0003)
                ))
        );
    }

    @Test
    public void shouldStartWithClearCache() {
        // WHEN
        LazyLocationLoader loader = new LazyLocationLoader(LOCATIONS_FOREST_GLADE_TEST);
        // THEN
        assertThat(loader.getCache()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("locationProvider")
    public void shouldLoadAndValidateLocations(String locationId, String expectedShort, String expectedLong, List<Exit> exits) throws IOException {
        // WHEN
        LazyLocationLoader loader = new LazyLocationLoader(LOCATIONS_FOREST_GLADE_TEST);
        BaseLocation forestGladeLocation = loader.getLocation(locationId);

        // THEN
        assertThat(forestGladeLocation).isNotNull();
        assertThat(forestGladeLocation.getShortDescription()).isEqualTo(expectedShort);
        assertThat(forestGladeLocation.getLongDescription()).isEqualTo(expectedLong);
        assertThat(forestGladeLocation.getExits()).containsExactlyInAnyOrderElementsOf(exits);
    }

    @Test
    public void shouldStoreLocationsInCache() throws IOException {
        // WHEN
        LazyLocationLoader loader = new LazyLocationLoader(LOCATIONS_FOREST_GLADE_TEST);
        loadAndAssertProperId(loader, FOREST_GLADE_0001);

        // THEN
        assertThat(loader.getCache()).hasSize(1);

        // WHEN
        loadAndAssertProperId(loader, FOREST_GLADE_0001);

        // THEN
        assertThat(loader.getCache()).hasSize(1);  //Still 1.

        // WHEN
        loadAndAssertProperId(loader, FOREST_GLADE_0002);
        loadAndAssertProperId(loader, FOREST_GLADE_0003);
        loadAndAssertProperId(loader, FOREST_GLADE_0002);
        loadAndAssertProperId(loader, FOREST_GLADE_0004);

        // THEN
        assertThat(loader.getCache()).hasSize(4);
    }

    @Test
    public void shouldThrowExceptionWhenFileIsNotFound() {
        // WHEN
        LazyLocationLoader loader = new LazyLocationLoader(LOCATIONS_FOREST_GLADE_TEST);
        Throwable thrown = catchThrowable(() -> loader.getLocation(INVALID_FILENAME));

        // THEN
        assertThat(thrown)
            .isInstanceOf(FileNotFoundException.class)
            .hasMessageContaining(MISSING_FILE);
    }

    private void loadAndAssertProperId(LazyLocationLoader loader, String targetId) throws IOException {
        BaseLocation location = loader.getLocation(targetId);
        assertThat(location).isNotNull();
        assertThat(location.getId()).isEqualTo(targetId);
    }

}
