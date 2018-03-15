package com.example.demo;

import com.example.demo.aggregates.Complaint;
import com.example.demo.commands.FileComplaintCommand;
import com.example.demo.events.ComplaintFiledEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests extends IntegrationContainerEnvironments {


	private AggregateTestFixture<Complaint> fixture;


	@Before
	public void setup() {
		fixture = new AggregateTestFixture<>(Complaint.class);
	}

	@Test
	public void axonTest() throws Exception {

		fixture.givenNoPriorActivity()
				.when(new FileComplaintCommand("1", "Apple", "Description"))
				.expectEvents(new ComplaintFiledEvent("1", "Apple", "Description"));
	}



	@Test
	public void contextLoads() {
		System.out.println("Hello");
	}

}
