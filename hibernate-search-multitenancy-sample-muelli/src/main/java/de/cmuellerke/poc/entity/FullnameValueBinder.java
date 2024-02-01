package de.cmuellerke.poc.entity;

import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class FullnameValueBinder implements ValueBinder{

	@Override
	public void bind(ValueBindingContext<?> context) {
		context.bridge(
				String.class,
				new FullnameValueBridge(),
				context.typeFactory().extension(ElasticsearchExtension.get())
				.asNative()
				.mapping("{\"type\": \"search_as_you_type\"}")
				);
	}

	private static class FullnameValueBridge implements ValueBridge<String, JsonElement> {
        @Override
        public JsonElement toIndexedValue(String value,
                ValueBridgeToIndexedValueContext context) {
            return value == null ? null : new JsonPrimitive( value ); 
        }

        @Override
        public String fromIndexedValue(JsonElement value,
                ValueBridgeFromIndexedValueContext context) {
            return value == null ? null : value.getAsString(); 
        }
	}
}
