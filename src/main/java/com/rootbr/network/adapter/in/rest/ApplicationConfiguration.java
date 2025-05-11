package com.rootbr.network.adapter.in.rest;

import java.util.Arrays;
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
      compositeConfiguration.addConfiguration(new EnvironmentConfiguration());
      configuration = builder.getConfiguration();
      compositeConfiguration.addConfiguration(configuration);
      configuration.getKeys().forEachRemaining(key ->
          configuration.setProperty(key, compositeConfiguration.getProperty(key))
      );
    } catch (ConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public Configuration subset(final String prefix) {
    return configuration.subset(prefix);
  }

  public String getString(final String key) {
    return configuration.getString(key);
  }

  public int getInt(final String key) {
    return configuration.getInt(key);
  }

  public Properties properties(final String key) {
    return ConfigurationConverter.getProperties(configuration.subset(key));
  }
}
