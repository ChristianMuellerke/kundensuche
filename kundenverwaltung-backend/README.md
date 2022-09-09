### Neu

Ich habe die Anwendung nochmal neu aufgesetzt, diesmal erstmal mit dem Ziel des Usermanagements.
Danach fehlt alles wieder was wir vorher hatten: Multi-Tenancy, aber auch die Kundenobjekte habe 
ich nicht mehr.

Zuletzt hatte ich Probleme noch die User anzulegen, der hatte die Rollen nicht in der Datenbank
und ich vermute die muss man selber dort erst anlegen.

### Verworfen
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
- Security so erweitern, dass sie die User aus der Datenbank bezieht: https://www.javainuse.com/spring/boot-jwt-mysql -> done
- das mit den Usern aus der Datenbank scheint soweit zu klappen, aber folgendes ist noch offen:
 - der Tenant sollte Bestandteil des Tokens sein -> wie machen wir das? Der Tenant soll dann auch beim Datenbankzugriff aus dem Token gezogen werden -> wie machen wir das? Wie kommen wir jederzeit an das Token dran?
 - Wir haben noch keinen Test, der die Autorisierung prüft (ein Token holt)
 - Wann wird das Token eigentlich invalidiert bzw. braucht es eine bestimmte TTL?
 - alle Integrations Tests brauchen jetzt irgendwie die Security, das ist beim Testen doof -> https://www.javachinna.com/disable-spring-security-or-mock-authentication-junit-tests/
 
Prozess: Bevor jemand einen REST-Endpunkt rufen kann, muss er sich ein UserToken holen und dieses im Header mitgeben. Im Token muss die UserId, sein Name und der Tenant enthalten sein. Zur Laufzeit holen wir uns dann den Tenant aus dem Token.

### Letzter Stand

*Done*: Damit wir Benutzer nicht nur mit Username und Password identifizieren, sondern auch mit einem Tenant, müssen wir eine eigene Implementierung von org.springframework.security.core.userdetails.User (eine Ableitung dieser Klasse?) erschaffen, in der man auch den Tenant transportieren kann. Statt dann den Tenant im HttpHeader zu transportieren, können wir uns den zur Laufzeit dann aus dem Token jeweils holen.

*Done*: https://stackoverflow.com/questions/66278129/mocking-jwt-token-in-springboottest-with-webtestclient

Der de.cmuellerke.demo.controller.UserControllerIntegrationTest muss so gebaut werden, dass der WebTestClient einen MockUser verwendet. Wahrscheinlich macht es Sinn, den Test zu teilen. (12.08.2022)

*Next*:
- Switch nach Spring Boot 2.7.2 war ein ziemlicher Flop, jetzt geht gar nix mehr (Circular Dependencies etc).
- Wir fangen nochmal von vorne an: Die Spring jwt Sample Anwendung nochmal als Basis nehmen und dann die MultiTenancy darauf adaptieren. Und danach bauen wir noch eine UI für den Login.
- siehe auch 
-- https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
-- https://www.bezkoder.com/angular-12-spring-boot-jwt-auth/



*Next*: 
- UserManagementTests ausbauen
- Customer Management bauen
- UI bauen: Login mit User, Tenant und Passwort und danach irgendwas fachliches zur Anzeige bringen -> UserManagement
- Bulk Import von Testkundendaten
- Trigger via Kafka einbauen für Kundendaten (Customer): Neu, Änderung, Löschung, Neuaufsetzen ebenso
- Consumerseite bauen:
-- eine Seite soll einmal ElasticSearch sein
-- eine andere Seite soll eine Suche mit Hibernate Search 6.x sein - Datenbank soll dann In-Memory sein

### Ziele

Ich will eine Datenbank haben. Diese soll danach via Kafka die Kunden und die Änderungen an den Kunden spiegeln und das ELK Backend soll das dann danach fangen

Mit der Datenbank will dann Performance Tests machen, also wie schnell gehen manche Sachen...und was kann ich dann am ORM lernen?

Wichtig ist die Multi-Tenancy durchzuziehen

https://medium.com/swlh/multi-tenancy-implementation-using-spring-boot-hibernate-6a8e3ecb251a
https://callistaenterprise.se/blogg/teknik/2020/10/17/multi-tenancy-with-spring-boot-part5/
https://github.com/callistaenterprise/blog-multitenancy/tree/shared_database_hibernate

