package ltg.workshop.meza;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.meza.aao.ActorManager;
import hu.meza.aao.DefaultScenarioContext;
import hu.meza.aao.ScenarioContext;
import ltg.workshop.meza.actions.ReadDirectMessagesAction;
import ltg.workshop.meza.actions.SendDirectMessageAction;
import org.junit.Assert;

import java.util.Map;

public class MessagingSteps {

	private ActorManager actorManager;
	private ScenarioContext context;

	public MessagingSteps(ActorManager actorManager, DefaultScenarioContext context) {
		this.actorManager = actorManager;
		this.context = context;
	}

	@When("^(.*) sends a DM to (.*)$")
	public void sendsDirectMessageTo(String actor1Label, String actor2Label) {
		User user1 = (User) actorManager.getActor(actor1Label);
		User user2 = (User) actorManager.getActor(actor2Label);

		final String randomMessage = getRandomMessage();
		DirectMessage message = new DirectMessage(user1.getUsername(), randomMessage);

		context.setSubject(message);

		SendDirectMessageAction action = new SendDirectMessageAction(message, user2.getUsername());
		user1.execute(action);

	}

	@Then("^(.*) should see (.*)’s message$")
	public void shouldSeeMessage(String actor1Label, String actor2Label) {
		User user1 = (User) actorManager.getActor(actor1Label);
		User user2 = (User) actorManager.getActor(actor2Label);

		DirectMessage message = context.getSubject();

		ReadDirectMessagesAction action = new ReadDirectMessagesAction();

		user1.execute(action);

		String errorMsg = String.format("%s did not receive %s's message", actor2Label, actor1Label);
		final Map<String, String> messages = action.messages();
		Assert.assertTrue(errorMsg, messages.containsKey(message.getSender()));

		String receivedMessage = messages.get(message.getSender());

		Assert.assertEquals(message.getMessage(), receivedMessage);

	}

	private String getRandomMessage() {
		String prefix = "Random message";
		String time = String.valueOf(System.currentTimeMillis());

		String message = String.format("%s %s", prefix, time);
		return message;

	}
}
