package com.tfgm.persistence;


import com.tfgm.models.TramNetworkDTO;

import java.util.List;

/**
 *
 */
public interface TramNetworkDTORepo {
    List<TramNetworkDTO> getAll();
}
