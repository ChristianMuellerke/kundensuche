### Als nächstes

* Letzter Stand:
  * Login geht und man kann dann dort auf der Übersichtsseite auch die Benutzer aus der Datenbank sehen
  * Wir bauen jetzt das CRUD für Kunden auf
  * diese Endpunkte kann dann ein Testtreiber befeuern
  * ich wollte die Multi-Tenancy jetzt schon wieder umbauen, denn das hier sieht viel einfacher aus:
    - https://github.com/spring-projects/spring-data-examples/blob/main/jpa/multitenant/partition/src/test/java/example/springdata/jpa/hibernatemultitenant/partition/ApplicationTests.java
    - https://spring.io/blog/2022/07/31/how-to-integrate-hibernates-multitenant-feature-with-spring-data-jpa-in-a-spring-boot-application
    
  * die Multitenancy habe ich entsprechend umgebaut, es funktioniert jetzt auch. Dieser Branch muss jetzt zum master werden. Allerdings habe ich auf dem master auch Dinge getrieben, so dass ein einfacher merge nicht reichen wird -> ich muss das mit der merge strategy ours mergen, siehe https://stackoverflow.com/questions/2763006/make-the-current-git-branch-a-master-branch
 

### Ziele

Ich will eine Datenbank haben. Diese soll danach via Kafka die Kunden und die Änderungen an den Kunden spiegeln und das ELK Backend soll das dann danach fangen

Mit der Datenbank will dann Performance Tests machen, also wie schnell gehen manche Sachen...und was kann ich dann am ORM lernen?

Wichtig ist die Multi-Tenancy durchzuziehen

https://medium.com/swlh/multi-tenancy-implementation-using-spring-boot-hibernate-6a8e3ecb251a
https://callistaenterprise.se/blogg/teknik/2020/10/17/multi-tenancy-with-spring-boot-part5/
https://github.com/callistaenterprise/blog-multitenancy/tree/shared_database_hibernate

### Links

* https://medium.com/@majdasab/integrating-an-angular-project-with-spring-boot-e3a043b7307b
