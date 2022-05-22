package com.kit.wb.wbbot.cronJob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ScheduledFuture;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CronInfoEntity {

	boolean isRepeated;
	long time;
	String text;
	ScheduledFuture<?> task;
}
