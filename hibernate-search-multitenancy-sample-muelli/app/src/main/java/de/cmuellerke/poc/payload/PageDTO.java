package de.cmuellerke.poc.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
	private List<T> content;
	private long total;
}
