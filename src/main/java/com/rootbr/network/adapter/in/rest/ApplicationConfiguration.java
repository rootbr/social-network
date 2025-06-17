package com.rootbr.network.adapter.in.rest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.BasePathLocationStrategy;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;
import org.apache.commons.configuration2.io.ProvidedURLLocationStrategy;

public class ApplicationConfiguration {
  private final FileBasedConfigurationBuilder<YAMLConfiguration> builder;
  private final YAMLConfiguration configuration;

  public ApplicationConfiguration(final String fileName) {
    try {
      builder = new FileBasedConfigurationBuilder<>(YAMLConfiguration.class)
          .configure(
              new Parameters()
                  .hierarchical()
                  .setThrowExceptionOnMissing(false)
                  .setFileName(fileName)
                  .setLocationStrategy(
                      new CombinedLocationStrategy(
                          Arrays.asList(
                              new ProvidedURLLocationStrategy(),
                              new FileSystemLocationStrategy(),
                              new BasePathLocationStrategy(),
                              new ClasspathLocationStrategy()
                          )
                      )
                  )
          );
      final CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
      compositeConfiguration.addConfiguration(new SystemConfiguration());
      final EnvironmentConfiguration envConfig = new EnvironmentConfiguration();
      compositeConfiguration.addConfiguration(envConfig);
      configuration = builder.getConfiguration();
      compositeConfiguration.addConfiguration(configuration);
      configuration.getKeys().forEachRemaining(key -> {
        final String envKey = key.replace(".", "_").toUpperCase();
        if (envConfig.containsKey(envKey)) {
          configuration.setProperty(key, envConfig.getProperty(envKey));
        }
      });
    } catch (final ConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public Properties properties(final String key) {
    return ConfigurationConverter.getProperties(configuration.subset(key));
  }
}
