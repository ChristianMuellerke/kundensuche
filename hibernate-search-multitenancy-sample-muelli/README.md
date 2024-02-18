
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

     
### WebFrontend

 * we need a tenant list/ tenant selection
 * search by forename, familyname, 
 * full-name search with search-as-you-type
 * search results with paging
 