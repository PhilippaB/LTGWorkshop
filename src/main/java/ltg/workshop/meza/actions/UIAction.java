package ltg.workshop.meza.actions;

import hu.meza.aao.Action;
import org.openqa.selenium.WebDriver;

public interface UIAction extends Action {

	public void useDriver(WebDriver driver);

}
