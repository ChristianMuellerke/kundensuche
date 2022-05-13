package de.cmuellerke.kundensuche.entity;

import org.elasticsearch.common.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.core.completion.Completion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "kundenindex")
@Setting(settingPath = "es-config/elastic-analyzer.json")
@Getter
@Setter
@NoArgsConstructor
public class Kunde {

	@Id
	private String id;

	@Field(type = FieldType.Text, name = "kurzname")
	private String kurzname;

	@Nullable
	@CompletionField(maxInputLength = 200)
	private Completion suggest;

	@Field(type = FieldType.Long, name = "personennummer")
	private Long personennummer;

	@Field(type = FieldType.Text, name = "vorname", analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
	private String vorname;

	@Field(type = FieldType.Text, name = "nachname")
	private String nachname;

	@Field(type = FieldType.Text, name = "strasse")
	private String strasse;

	@Field(type = FieldType.Text, name = "ort")
	private String ort;

	@Field(type = FieldType.Integer, name = "plz")
	private int postleitzahl;

	@Field(type = FieldType.Keyword, name = "konten")
	private String konten;

}
