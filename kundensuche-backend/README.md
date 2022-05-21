# Kundensuche

## Über

Ich versuche eine Kundensuche mit Elasticsearch zu bauen.

## Links

* https://reflectoring.io/spring-boot-elasticsearch/
* https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html
* https://spinscale.de/posts/2020-08-06-introduction-into-spring-data-elasticsearch.html
* https://codecurated.com/blog/how-to-connect-java-with-elasticsearch/

* https://medium.com/@milosbiljanovic/springboot-autocomplete-with-elasticsearch-11ea95d58854
* https://github.com/thelivelock/elasticsearch-autocomplete

* https://dzone.com/articles/introduction-to-spring-data-elasticsearch-41

* http://localhost:9200/_cat/indices
* http://localhost:8080/sayt?kurzname=Chri
* https://coralogix.com/blog/elasticsearch-autocomplete-with-search-as-you-type/
* https://www.elastic.co/guide/en/elasticsearch/reference/current/search-as-you-type.html
* https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.mapping

## ELK starten

Wir brauchen `docker` und `docker-compose`

    # als root setzen:
    sysctl -w vm.max_map_count=262144
    
    # als normaler user
    ssh 192.168.178.130 -l cm
    cd ~/develop/elk/ && docker-compose up

## ELK Abfragen

### Allgemein funktionsfähig?

    http://192.168.178.130:9200/
    
    
```json
{
  "name" : "es01",
  "cluster_name" : "es-docker-cluster",
  "cluster_uuid" : "w4wn3t5VRl64tpSBlEW-9A",
  "version" : {
    "number" : "7.13.4",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "c5f60e894ca0c61cdbae4f5a686d9f08bcefc942",
    "build_date" : "2021-07-14T18:33:36.673943207Z",
    "build_snapshot" : false,
    "lucene_version" : "8.8.2",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

### Alle Indizes abfragen

    http://192.168.178.130:9200/_cat/indices?v

Dann muss in etwa sowas da stehen:

    health status index       uuid                   pri rep docs.count docs.deleted store.size pri.store.size
    green  open   kundenindex nrx8laK2Qp6nDxNmw3LFBw   1   1      95331            0      6.3mb          3.1mb
    green  open   hello-world OXVIp33BSN2n-F5qokJXLA   1   0          0            0       208b           208b

## Suchen

    http://localhost:8080/suggestVorname?vorname=Chri
    
    

## Kundendaten importieren

Die Kundendaten werden generiert und können dann via REST-API eingespielt werden. 

### API Client generieren
Die OpenApi Spec ist unter http://localhost:8080/api-docs.yaml abzurufen, dann im Projekt zu aktualisieren und dann `mvn generate-sources`

### cli bauen

    $ cd kundenverwaltung/kundenverwaltung-client/
    $ mvn clean package
    
Beim Bauen wird auch die OpenAPI für den Clientcode gegen die API generiert.

### Begrenzte Anzahl Kundendaten importieren

    java -jar ./target/kundenverwaltung-rest-client-0.0.1-SNAPSHOT.jar import 10

### Viele Kundendaten importieren

Achtung: Das kann durchaus Zeit in Anspruch nehmen

    java -jar ./target/kundenverwaltung-rest-client-0.0.1-SNAPSHOT.jar batchImport


