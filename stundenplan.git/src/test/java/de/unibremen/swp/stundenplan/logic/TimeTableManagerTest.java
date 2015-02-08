package de.unibremen.swp.stundenplan.logic;

import static org.junit.Assert.*;

import java.io.IOException;

import javassist.compiler.ast.NewExpr;

import org.junit.Before;
import org.junit.Test;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.gui.Timeslot;

public class TimeTableManagerTest {
	
	@Before
	public void beforetest(){
		try {
			Config.init(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPEConversion() {
		Planungseinheit pe = new Planungseinheit();
		pe.setStarthour(10);
		pe.setStartminute(0);
		pe.setEndhour(11);
		pe.setEndminute(0);
		int peints = pe.duration()/Timeslot.timeslotlength();
		assertEquals(TimetableManager.planungsEinheitToTimeslot(pe).size(), peints);
	}

	@Test
	public void testStartTimeMinute() {
		assertEquals(TimetableManager.startTimeMinute(), Config.getInt(Config.DAY_STARTTIME_MINUTE_STRING, Config.DAY_STARTTIME_MINUTE));
	}

	@Test
	public void testStartTimeHour() {
		assertEquals(TimetableManager.startTimeHour(), Config.getInt(Config.DAY_STARTTIME_HOUR_STRING, Config.DAY_STARTTIME_HOUR));
	}

	@Test
	public void testEndTimeMinute() {
		assertEquals(TimetableManager.endTimeMinute(), Config.getInt(Config.DAY_ENDTIME_MINUTE_STRING, Config.DAY_ENDTIME_MINUTE));
	}

	@Test
	public void testEndTimeHour() {
		assertEquals(TimetableManager.endTimeHour(), Config.getInt(Config.DAY_ENDTIME_HOUR_STRING, Config.DAY_ENDTIME_HOUR));
	}

	@Test
	public void testDuration() {
		assertEquals(TimetableManager.duration(10,0,11,0),60);
		assertEquals(TimetableManager.duration(10,15,11,0),45);
		assertEquals(TimetableManager.duration(14,0,14,5),5);
		assertEquals(TimetableManager.duration(15,0,14,0),60);
	}

	@Test
	public void testDaytablelength() {
		int testvalue = TimetableManager.duration(TimetableManager.startTimeHour(),
				TimetableManager.startTimeMinute(),
				TimetableManager.endTimeHour(),
				TimetableManager.endTimeMinute());
		testvalue = testvalue/Timeslot.timeslotlength();
		assertEquals(TimetableManager.daytablelength(),testvalue
				);
	}

}
