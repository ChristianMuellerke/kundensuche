
Es fehlt:
- Momentan kann man auch ohne Tenant was machen, aber das müssten wir abfangen
- Man sollte auch nur mit einem in der Tabelle Tenants bestehenden Tenant was machen dürfen. Das müssten wir abfangen
   - erstmal per DB Verknüpfung?
   - danach per Caching: Alle Tenants sollten sich im Cache befinden, damit man den DB Call spart?
   
- Danach können wir Benutzer pro Tenant anlegen. Wir brauchen auch Kunden pro Tenant
- Letzter Stand: Die UnitTests wurden wieder korrigiert, die Tabelle Tenants ist neu
 Aktuell macht es keinen Sinn das schon gegen eine echte DB laufen zu lassen. Ausserdem musste ich die Schema-Validierung deaktivieren, das hat nicht mehr funktioniert und ich weiss noch nicht warum
- es macht Sinn das auf Spring Boot 2.7.1 zu heben, sobald das da ist
- Wir müssen uns ggü. der REST API via JWT autorisieren. Ich war da zuletzt dran. Ziel ist es, dass ein User sich nur an seinem eigenen Tenant anmelden kann um dort etwas zu tun. Rechteverwaltung lassen wir aussen vor (jeder darf erstmal alles) Anleitung: https://www.javainuse.com/spring/boot-jwt [DONE]
- Security so erweitern, dass sie die User aus der Datenbank bezieht: https://www.javainuse.com/spring/boot-jwt-mysql

Prozess: Bevor jemand einen REST-Endpunkt rufen kann, muss er sich ein UserToken holen und dieses im Header mitgeben. Im Token muss die UserId, sein Name und der Tenant enthalten sein. Zur Laufzeit holen wir uns dann den Tenant aus dem Token.

### Ziele

Ich will eine Datenbank haben. Diese soll danach via Kafka die Kunden und die Änderungen an den Kunden spiegeln und das ELK Backend soll das dann danach fangen

Mit der Datenbank will dann Performance Tests machen, also wie schnell gehen manche Sachen...und was kann ich dann am ORM lernen?

Wichtig ist die Multi-Tenancy durchzuziehen

https://medium.com/swlh/multi-tenancy-implementation-using-spring-boot-hibernate-6a8e3ecb251a
https://callistaenterprise.se/blogg/teknik/2020/10/17/multi-tenancy-with-spring-boot-part5/
https://github.com/callistaenterprise/blog-multitenancy/tree/shared_database_hibernate

