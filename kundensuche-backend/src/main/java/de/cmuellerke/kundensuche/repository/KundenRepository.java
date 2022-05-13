package de.cmuellerke.kundensuche.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundensuche.entity.Kunde;

@Repository
public interface KundenRepository extends ElasticsearchRepository<Kunde, String> {

	Page<Kunde> findByKurzname(String kurzname, Pageable pageable);

	Page<Kunde> findByKurznameContaining(String kurzname, Pageable pageable);

	Page<Kunde> findByVorname(String kurzname, Pageable pageable);

	Page<Kunde> findByNachname(String kurzname, Pageable pageable);

}
