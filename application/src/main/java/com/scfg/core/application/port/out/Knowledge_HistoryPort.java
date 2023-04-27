package com.scfg.core.application.port.out;

import com.scfg.core.domain.Knowledge_History;

import java.util.List;

public interface Knowledge_HistoryPort {
  List<Knowledge_History> findall();
  boolean guardar(List<Knowledge_History> list);
}
