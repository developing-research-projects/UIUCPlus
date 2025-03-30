package com.fasterxml.jackson.databind.deser.dos;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.BaseMapTest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class DeepNestingUntypedDeserTest extends BaseMapTest {

    private final static int TOO_DEEP_NESTING = 100_000;

    // Configure ObjectMapper with custom StreamReadConstraints
    private final ObjectMapper MAPPER;

    public DeepNestingUntypedDeserTest() {
        JsonFactory jsonFactory = JsonFactory.builder()
            .streamReadConstraints(StreamReadConstraints.builder()
                .maxNestingDepth(200_000)  // Increase the depth as needed
                .build())
            .build();
        MAPPER = JsonMapper.builder(jsonFactory).build();
    }

    public void testFormerlyTooDeepUntypedWithArray() throws Exception {
        final String doc = _nestedDoc(TOO_DEEP_NESTING, "[ ", "] ");
        Object ob = MAPPER.readValue(doc, Object.class);
        assertTrue(ob instanceof List<?>);

        // ... but also work with Java array
        ob = MAPPER.readerFor(Object.class)
                .with(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)
                .readValue(doc, Object.class);
        assertTrue(ob instanceof Object[]);
    }

    public void testFormerlyTooDeepUntypedWithObject() throws Exception {
        final String doc = "{"+_nestedDoc(TOO_DEEP_NESTING, "\"x\":{", "} ") + "}";
        Object ob = MAPPER.readValue(doc, Object.class);
        assertTrue(ob instanceof Map<?, ?>);
    }

    private String _nestedDoc(int nesting, String open, String close) {
        StringBuilder sb = new StringBuilder(nesting * (open.length() + close.length()));
        for (int i = 0; i < nesting; ++i) {
            sb.append(open);
            if ((i & 31) == 0) {
                sb.append("\n");
            }
        }
        for (int i = 0; i < nesting; ++i) {
            sb.append(close);
            if ((i & 31) == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

