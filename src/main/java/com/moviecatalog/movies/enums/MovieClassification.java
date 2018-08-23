package com.moviecatalog.movies.enums;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MovieClassification {
  _12(12), _12A(12), _15(15), _18(18), _18A(18);
  
  private final int minAge;
  
  public static Stream<MovieClassification> stream() {
      return Stream.of(MovieClassification.values()); 
  }
}