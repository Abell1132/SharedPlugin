package dev.shared.do_gamer.config;

import eu.darkbot.api.config.annotations.Dropdown;
import eu.darkbot.api.config.annotations.Number;
import eu.darkbot.api.config.annotations.Option;
import java.util.Arrays;
import java.util.List;

public class AutoDispatchConfig {
    @Option("general.enabled")
    public boolean enabled = false;

    @Option("do_gamer.auto_dispatch.interval")
    @Number(min = 1, max = 1440, step = 1)
    public int intervalMinutes = 15;

    @Option("do_gamer.auto_dispatch.retriever")
    @Dropdown(options = RetrieverOptions.class)
    public String retriever = "R-01";

    public static class RetrieverOptions implements Dropdown.Options<String> {
        @Override
        public List<String> options() {
            return Arrays.asList("R-01", "R-02", "R-03", "R-04", "R-05", "R-06",
                    "A-01", "A-02", "A-03", "A-04", "A-05", "A-06",
                    "B-01", "B-02", "B-03", "B-04", "B-05", "B-06");
        }
    }
}
