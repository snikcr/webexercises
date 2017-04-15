package practice.controller.serialization;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import practice.utils.Utils;

/**
 * Serializer for java.time.LocalDate
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

	@Override
	public void serialize(LocalDate localDate, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		gen.writeString(Utils.localDateToString(localDate));
	}

}
