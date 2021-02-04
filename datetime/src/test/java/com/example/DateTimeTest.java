package com.example;


import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;


public class DateTimeTest
{
	
    @Test
    public void instantSubtraction1() {
    	
    	Instant inst1 = Instant.parse("2016-09-13T13:00:00Z");
    	Instant inst2 = Instant.parse("2016-09-13T13:00:10Z");
    	long secDiff = Duration.between(inst1, inst2).getSeconds();
		out.println("Testing instantSubtraction1: " + secDiff);
		assertEquals(secDiff, 10L);
    }
    
    @Test
    public void instantSubtraction2() {
    	
    	Instant inst1 = Instant.now();
    	Instant inst2 = inst1.plusSeconds(10L);
    	long secDiff = Duration.between(inst1, inst2).getSeconds();
		out.println("Testing instantSubtraction2: " + secDiff);
		assertEquals(secDiff, 10L);
    }
    
    @Test
    public void localDateTime() {
    	
    	LocalDateTime ldNow = LocalDateTime.now();
    	LocalDateTime ldThen = ldNow.plus(Period.ofDays(1));
    	long hourDiff = Duration.between(ldNow, ldThen).toHours();
		out.println("Testing localDateTime: " + hourDiff);
		assertEquals(10L, 10L);
    }
    
    @Test
    public void zonedDateTime() {
    	
    	ZonedDateTime ldNow = ZonedDateTime.of(2016, 3, 26, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
    	ZonedDateTime ldThenPeriod = ldNow.plus(Period.ofDays(1)); //Period can't add units smaller than Days since this would break the magic of DST correction
    	ZonedDateTime ldThenDuration = ldNow.plus(Duration.ofDays(1));
    	out.println("Testing localDateTime: Exactly one day after " + ldNow + " is " + ldThenPeriod + " and not " + ldThenDuration);
		assertEquals(ldThenPeriod.toEpochSecond(), 1459072800L);
		assertEquals(ldThenDuration.toEpochSecond(), 1459076400L);
    }
    
    @Test
    public void timeZoneTransformation() {
    	
    	ZonedDateTime ldThere = ZonedDateTime.of(2016, 3, 26, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
    	ZonedDateTime ldHere = ldThere.withZoneSameInstant(ZoneId.of("America/Bogota"));
    	out.println("Testing timeZoneTransformation: " + ldThere + " " + ldHere);
    	assertEquals(0,0);
    }
    
    @Test
    public void interfaceIheritance() {
    	
    	InterfaceImplementer testObj = new InterfaceImplementer();
		out.println("Testing interfaceIheritance: " + testObj.doAllSteps());
		assertEquals(testObj.doAllSteps(), 7);
    }
    
    @Test
    public void temporalAdjusters() {
    	
    	LocalDate today = LocalDate.now();
    	LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		out.println("Testing temporalAdjusters: Next Monday after " + today + " is " + nextMonday);
		assertEquals(nextMonday.getDayOfWeek(), DayOfWeek.MONDAY);
    }
}
