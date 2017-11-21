package io.pivotal.earthquakes.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class ElasticSearchClient {

	private RestClient client;
	
	public ElasticSearchClient(String url) {
		
		String[] address = url.split(":");
		
		while (client==null) {
			client = RestClient
		            .builder(new HttpHost(address[0], Integer.parseInt(address[1])))
		            .build();
		}
	}
	
	public void insert(String json) throws UnsupportedEncodingException, IOException {
		
		HttpEntity requestBody  = new StringEntity(json);
		Response response = client.performRequest(
		        "POST",
		        "/earthquakes/earthquake",
		        new Hashtable<>(),
		        requestBody);
	}
	
	public void createIndex() throws IOException {
		
		Response response = client.performRequest(
		        "PUT",
		        "/earthquakes",
		        new Hashtable<>(),
		        new StringEntity(
		        "{" +
		        "  \"mappings\": {\n" + 
		        "    \"_default_\": {\n" + 
		        "      \"_all\": {\n" + 
		        "        \"enabled\": false\n" + 
		        "      },\n" + 
		        "      \"dynamic_templates\": [\n" + 
		        "        {\n" + 
		        "          \"strings\": {\n" + 
		        "            \"match\": \"*\",\n" + 
		        "            \"match_mapping_type\": \"string\",\n" + 
		        "            \"mapping\": {\n" + 
		        "              \"type\": \"keyword\"\n" + 
		        "            }\n" + 
		        "          }\n" + 
		        "        }\n" + 
		        "      ],\n" + 
		        "      \"properties\": {\n" + 
		        "        \"@timestamp\": {\n" + 
		        "          \"type\": \"date\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"beat\": {\n" + 
		        "          \"properties\": {\n" + 
		        "            \"hostname\": {\n" + 
		        "              \"type\": \"keyword\"\n" + 
		        "            },\n" + 
		        "            \"name\": {\n" + 
		        "              \"type\": \"keyword\"\n" + 
		        "            },\n" + 
		        "            \"version\": {\n" + 
		        "              \"type\": \"keyword\"\n" + 
		        "            }\n" + 
		        "          }\n" + 
		        "        },\n" + 
		        "        \"depth\": {\n" + 
		        "          \"type\": \"float\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"dmin\": {\n" + 
		        "          \"type\": \"float\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"event_id\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        },\n" + 
		        "        \"gap\": {\n" + 
		        "          \"type\": \"float\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"input_type\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        },\n" + 
		        "        \"location\": {\n" + 
		        "          \"type\": \"geo_point\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"mag\": {\n" + 
		        "          \"type\": \"float\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"magType\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        },\n" + 
		        "        \"message\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        },\n" + 
		        "        \"nst\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        },\n" + 
		        "        \"offset\": {\n" + 
		        "          \"type\": \"long\"\n" + 
		        "        },\n" + 
		        "        \"rms\": {\n" + 
		        "          \"type\": \"float\",\n" + 
		        "          \"ignore_malformed\": true\n" + 
		        "        },\n" + 
		        "        \"source\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        },\n" + 
		        "        \"type\": {\n" + 
		        "          \"type\": \"keyword\"\n" + 
		        "        }\n" + 
		        "      }\n" + 
		        "    }\n" + 
		        "  }," +
		        "  \"settings\": {\n" + 
		        "    \"index\": {\n" + 
		        "      \"refresh_interval\": \"10s\",\n" + 
		        "      \"number_of_shards\": \"1\",\n" + 
		        "      \"number_of_replicas\": \"0\"\n" + 
		        "    }\n" + 
		        "  }\n" +
		        "}"
		        ));
	}
}
