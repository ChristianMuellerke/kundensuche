
Als nächstes ist dran:

LATEST:
- ich habe die Anwendung auf mehrere Packages aufgeteilt:
  - zum einen ein api-Package: soll alle REST-Endpunkte enthalten
  - die eigentliche Anwendung, diese implementiert die API
  - ein Package api-test: das sind die API e2e-Tests, also ein Testclient den wir in JAVA schreiben. Ziel ist es, dass wir einen feign-client nachher haben, mit dem wir die verschiedenen Anwendungsbestandteile vertesten können

1) Ich wollte die Anwendung nun gegen den echten ElasticSearchCluster testen. (Profile default/dev)
 - dazu brauchen wir ein eigenes Zertifikat, dass wir im Cluster hinterlegen
 - dazu muss unsere anwendung diesem zertifikat auch vertrauen

2) danach müssten wir auch mal eine echte datenbank anbinden (profile default)

-> Keine funktionale Erweiterung erstmal mehr, wir machen danach mit der UI weiter

### Status
 * es handelt sich hier um ein Sample mit Hibernate Search
 
### TODO

     * Weitere Testmethoden
     * 
     * insert 100 customers in every tenant         -> done, see de.cmuellerke.poc.service.CustomerSearchServiceMultiTenancyIntegrationTest
     * do a mass index on all
     * 
     * refactor to english                          -> done
     * evaluate tenants from outside                -> means, that tenant list is not statically in application.yaml
     * restart on tenant list changes?
     * search suggestion
     * search-as-you-type                           -> done
     * implement a web frontend                     -> next!
     * paging!?                                     -> done?
     * disable spring open in view                  -> done

     
### WebFrontend (Angular)

 * generate client via OpenAPI
 * we need a tenant list/ tenant selection
 * search by forename, familyname, 
 * full-name search with search-as-you-type
 * search results with paging
 