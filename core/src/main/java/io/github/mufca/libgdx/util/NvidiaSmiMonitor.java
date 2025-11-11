package io.github.mufca.libgdx.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import regexodus.Pattern;

public final class NvidiaSmiMonitor {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s*,\\s*"); // lub nawet ","

    public static Optional<GpuStatus> query() {
        try {
            if (Files.exists(Path.of("/usr/bin/nvidia-smi"))) {
                Process process = new ProcessBuilder("/usr/bin/nvidia-smi",
                    "--query-gpu=memory.total,memory.used,utilization.gpu",
                    "--format=csv,noheader,nounits")
                    .start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = reader.readLine();
                    if (line != null && !line.isEmpty()) {
                        String[] parts = SPLIT_PATTERN.split(line.trim());
                        int total = Integer.parseInt(parts[0]);
                        int used = Integer.parseInt(parts[1]);
                        int utilization = Integer.parseInt(parts[2]);

                        return Optional.of(new GpuStatus(total, used, utilization));
                    }
                }
            }
            return Optional.empty();

        } catch (Exception e) {
            LogHelper.info(NvidiaSmiMonitor.class, e.getMessage());
            return Optional.empty();
        }
    }

    public record GpuStatus(int memoryTotalMiB, int memoryUsedMiB, int utilizationPercent) {

        @Override
        public String toString() {
            return "GPU: %d%%, VRAM: %d/%d MiB".formatted(utilizationPercent, memoryUsedMiB, memoryTotalMiB);
        }
    }
}