package it.eng.iot.configuration;

import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConfRabbitMQ extends ConfHelper {

	private static ConfHelper inner;

	private static final String BUNDLE_NAME = "configuration_rabbitmq";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private ConfRabbitMQ() {}

	public static ConfHelper getInstance() {
		if(inner == null) {
			synchronized (ConfHelper.class) {
				if(inner == null) {
					inner = new ConfRabbitMQ();
				}
			}
		}
		return inner;
	}

	@Override
	public String getString(String key) {
		Optional<String> prop = Optional.ofNullable(System.getenv(key));
		try {
			 return prop.orElse(RESOURCE_BUNDLE.getString(key));
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
