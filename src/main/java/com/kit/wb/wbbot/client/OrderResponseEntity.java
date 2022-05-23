
package com.kit.wb.wbbot.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderResponseEntity implements Serializable {

	@JsonProperty("number")
	private int number;

	@JsonProperty("date")
	private String date;

	@JsonProperty("lastChangeDate")
	private String lastChangeDate;

	@JsonProperty("supplierArticle")
	private String supplierArticle;

	@JsonProperty("techSize")
	private String techSize;

	@JsonProperty("barcode")
	private String barcode;

	@JsonProperty("quantity")
	private long quantity;

	@JsonProperty("totalPrice")
	private long totalPrice;

	@JsonProperty("discountPercent")
	private long discountPercent;

	@JsonProperty("warehouseName")
	private String warehouseName;

	@JsonProperty("oblast")
	private String oblast;

	@JsonProperty("incomeID")
	private long incomeID;

	@JsonProperty("odid")
	private long odid;

	@JsonProperty("nmId")
	private long nmId;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("category")
	private String category;

	@JsonProperty("brand")
	private String brand;

	@JsonProperty("isCancel")
	private boolean isCancel;

	@JsonProperty("cancel_dt")
	private String cancel_dt;

	@JsonProperty("gNumber")
	private String gNumber;

	@JsonProperty("sticker")
	private String sticker;

}
