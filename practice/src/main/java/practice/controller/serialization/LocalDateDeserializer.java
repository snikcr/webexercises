package practice.controller.serialization;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import practice.utils.Utils;

/**
 * Deserializer for java.time.LocalDate;
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {

		LocalDate localDate = Utils.stringToLocalDate(parser.getValueAsString());
		
		return localDate;
	}

}
