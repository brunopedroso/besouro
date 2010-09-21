package besouro.plugin;

import org.eclipse.ui.IStartup;


//TODO [rule] edits in document change -> agregate continuous changes (is it necessary?)

//TODO [rule] ive extracted a method and it didnt classify it as a refactoring.
//		probably its because of the substantial thing that in my case registered the prod-edit action
//[action] 09/04/2010 10:02:35 Romans.java REFACTOR ADD METHOD {static int translateDigit(String)}
//[action] 09/04/2010 10:02:35 Romans.java PRODUCTION {MI=+1(2), SI=+1(2), , FI=+89(332)}
//[action] 09/04/2010 10:02:39 RomansTest.java TEST OK
//[action] 09/04/2010 10:02:39 RomansTest
//[episode] production 2

//TODO [rule] its recognizing test-first many times


//TODO [rule] if i write a test, fail, comment the test out to refactor and test pass, it recognizes as tstAdd2, tstAdd1, tstAdd2, tstAdd1 


//TODO [rule] one more case where test-first was recognized as prod1
// if we edit test without increasing assertions or methods, its not recognized as test-adition
// what is modeled through this 'production' rules?!

public class BesouroPlugin implements IStartup {

	public void earlyStartup() {
		// this should register listeners
//		ListenersSet.getSingleton();
	}

}

