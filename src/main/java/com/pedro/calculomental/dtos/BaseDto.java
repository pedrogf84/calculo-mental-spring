package com.pedro.calculomental.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class BaseDto.
 */
public class BaseDto implements Serializable {

	/** The Constant serialVersionUID. */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;

	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
}
