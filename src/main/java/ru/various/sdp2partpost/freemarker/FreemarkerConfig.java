package ru.various.sdp2partpost.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

public class FreemarkerConfig {

    private static Configuration cfg = getConfiguration();

    public static Configuration getConfiguration() {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_22);
            try {
                cfg.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }

        return cfg;
    }
}
