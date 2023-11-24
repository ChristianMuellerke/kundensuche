### Als nächstes

- wir räumen in GitHub auf!
- wir versuchen uns doch an Hibernate Search

### Hinweis

Diese Version funktioniert mit MultiTenancy. Aber damit das klappt, muss nach einem Update von Spring Boot zwingend ein `mvn clean install` laufen. Sonst holt der sich nicht die richtigen AOP Dinge ran. Eclipse macht das bei einem Maven Update nicht!

### Ziele

Ich will eine Datenbank haben. Diese soll danach via Kafka die Kunden und die Änderungen an den Kunden spiegeln und das ELK Backend soll das dann danach fangen

Mit der Datenbank will dann Performance Tests machen, also wie schnell gehen manche Sachen...und was kann ich dann am ORM lernen?

Wichtig ist die Multi-Tenancy durchzuziehen

https://medium.com/swlh/multi-tenancy-implementation-using-spring-boot-hibernate-6a8e3ecb251a
https://callistaenterprise.se/blogg/teknik/2020/10/17/multi-tenancy-with-spring-boot-part5/
https://github.com/callistaenterprise/blog-multitenancy/tree/shared_database_hibernate

### Links

* https://medium.com/@majdasab/integrating-an-angular-project-with-spring-boot-e3a043b7307b
