package br.com.ibico.api.entities;

import java.util.List;

public record Response<T>(List<T> items,
                          int pageNo,
                          int pageSize,
                          int totalElements,
                          int totalPages,
                          boolean last,
                          boolean self) {
}
