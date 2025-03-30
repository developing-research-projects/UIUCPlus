package com.fasterxml.jackson.databind.deser.dos;

import com.fasterxml.jackson.databind.BaseMapTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class DeepJsonNodeSerTest extends BaseMapTest
{
   private final ObjectMapper MAPPER;
	    
    public DeepJsonNodeSerTest() {
        JsonFactory jsonFactory = JsonFactory.builder()
            .streamReadConstraints(StreamReadConstraints.builder()
                .maxNestingDepth(200_000)
                .build())
            .build();
        MAPPER = JsonMapper.builder(jsonFactory).build();
    }
    

    public void testVeryDeepNodeSer() throws Exception
    {
        int depth = 9000;
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("{");

        for (int i=0; i < depth; i++) {
          jsonString.append(String.format("\"abc%s\": {", i));
        }

        for (int i=0; i < depth; i++) {
          jsonString.append("}");
        }

        jsonString.append("}");

        JsonNode jsonNode = MAPPER.readTree(jsonString.toString());
        String json = jsonNode.toString();
        assertNotNull(json);
    }
}
