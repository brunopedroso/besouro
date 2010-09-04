package athos.plugin;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;

import athos.listeners.JUnitListener;
import athos.listeners.JavaStructureChangeListener;
import athos.listeners.ResourceChangeListener;
import athos.listeners.WindowListener;
import athos.stream.ActionOutputStream;
import athos.stream.EpisodeClassifierStream;



//TODO  do not recognize testAssertionAdd + junitFail + producion + junitPass as test first

//TODO  not recognizing test edits
//(MAIN::initial-fact)
//(MAIN::UnaryRefactorAction (index 2) (file "RomanNumeralsTest.java") (operation "ADD") (type "FIELD") (data "void fr"))
//(MAIN::UnaryRefactorAction (index 3) (file "RomanNumeralsTest.java") (operation "RENAME") (type "FIELD") (data "fr => void firstTest"))
//(MAIN::UnaryRefactorAction (index 4) (file "RomanNumeralsTest.java") (operation "RENAME") (type "METHOD") (data "firstTest => void firstTest()"))
//(MAIN::ProductionEditAction (index 5) (file "RomanNumeralsTest.java") (duration 0) (byteChange 37) (methodChange 1) (statementChange 0))
//(MAIN::UnitTestEditAction (index 6) (file "RomanNumeralsTest.java") (duration 0) (byteChange 5) (testChange 1) (assertionChange 0))

//TODO  make tostrings look easier to recognize

//TODO  ive extracted a method and it doesnt classified it as a refactoring.
//		probably its becausa of the substantial thing that in my case registered the prod-edit action
//[action] 09/04/2010 10:02:35 Romans.java REFACTOR ADD METHOD {static int translateDigit(String)}
//[action] 09/04/2010 10:02:35 Romans.java PRODUCTION {MI=+1(2), SI=+1(2), , FI=+89(332)}
//[action] 09/04/2010 10:02:39 RomansTest.java TEST OK
//[action] 09/04/2010 10:02:39 RomansTest
//[episode] production 2

//TODO [rule] its recognizing test-first many times
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomanNumeralsKata/src/RomanNumeral.java (size: 425)(m: 2)(s: 8)(ta: 0)(ts: 0)
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomanNumeralsKata/test/RomanNumeralsTest.java (size: 691)(m: 3)(s: 8)(ta: 8)(ts: 3)
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomanNumeralsKata/test/AnotherTest.java (size: 165)(m: 1)(s: 1)(ta: 1)(ts: 1)
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomansKata2/test/RomansTest.java (size: 30)(m: 0)(s: 0)(ta: 0)(ts: 0)
//[action] 09/04/2010 09:56:07 RomansTest.java REFACTOR ADD FIELD {void test}
//[action] 09/04/2010 09:56:12 RomansTest.java REFACTOR RENAME METHOD {test => void testBareNumbers()}
//[action] 09/04/2010 09:56:16 RomansTest.java REFACTOR ADD FIELD {publ ic}
//[action] 09/04/2010 09:56:18 RomansTest.java REFACTOR REMOVE FIELD {ic}
//[action] 09/04/2010 09:56:24 RomansTest.java REFACTOR ADD IMPORT {import org.junit.Test}
//[action] 09/04/2010 09:56:31 RomansTest.java REFACTOR ADD IMPORT {import junit.framework.Assert}
//[action] 09/04/2010 09:56:53 RomansTest.java TEST {TI=+1(1), AI=+1(1)MI=+1(1), SI=+1(1), , FI=+149(179)}
//[action] 09/04/2010 09:56:54 RomansTest.java COMPILE {Romans cannot be resolved}
//[action] 09/04/2010 09:57:04 RomansTest.java COMPILE {The method toNumber(String) is undefined for the type Romans}
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomansKata2/src/Romans.java (size: 26)(m: 0)(s: 0)(ta: 0)(ts: 0)
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomansKata2/src/Romans.java (size: 26)(m: 0)(s: 0)(ta: 0)(ts: 0)
//[action] 09/04/2010 09:57:10 Romans.java REFACTOR ADD METHOD {static Object toNumber(String)}
//[action] 09/04/2010 09:57:11 Romans.java PRODUCTION {MI=+1(1), SI=+1(1), , FI=+104(130)}
//[action] 09/04/2010 09:57:25 RomansTest.java TEST FAILED
//[action] 09/04/2010 09:57:25 RomansTest
//[action] FileOpened: /Users/Bruno/runtime-EclipseApplication/RomansKata2/src/Romans.java (size: 93)(m: 1)(s: 1)(ta: 0)(ts: 0)
//[action] 09/04/2010 09:57:37 Romans.java PRODUCTION {MI=0(1), SI=0(1), , FI=-6(87)}
//[action] 09/04/2010 09:57:41 RomansTest.java TEST OK
//[action] 09/04/2010 09:57:41 RomansTest
//[episode] test-first 1
//[episode] test-first 1
//[episode] test-first 1
//[episode] test-first 1

//TODO if i write a test, fail, comment the test out to refactor and test pass, it recognizes as tstAdd2, tstAdd1, tstAdd2, tstAdd1 
//[action] 09/04/2010 10:19:54 RomansTest.java REFACTOR ADD METHOD {void testSimpleSumCases()/2}
//[action] 09/04/2010 10:19:58 RomansTest.java REFACTOR RENAME METHOD {testSimpleSumCases()/2 => void testSubCases()}
//[action] 09/04/2010 10:20:02 RomansTest.java REFACTOR RENAME METHOD {testSubCases() => void testSubtractionCases()}
//[action] 09/04/2010 10:20:08 RomansTest.java TEST {TI=+1(3), AI=+1(9)MI=+1(3), SI=+1(9), , FI=+98(674)}
//[action] 09/04/2010 10:20:10 RomansTest.java TEST FAILED
//[action] 09/04/2010 10:20:10 RomansTest
//[action] 09/04/2010 10:20:51 RomansTest.java TEST {TI=0(3), AI=0(9)MI=0(3), SI=0(9), , FI=+2(676)}
//[action] 09/04/2010 10:20:53 RomansTest.java TEST OK
//[action] 09/04/2010 10:20:53 RomansTest
//[episode] test-addition 2
//[episode] test-addition 1
//[episode] test-addition 2
//[episode] test-addition 1


//TODO  one more case where test-first war recognized as prod1
// seems to be the negative FI in the test-edit
//[action] 09/04/2010 10:23:33 RomansTest.java TEST {TI=0(3), AI=0(9)MI=0(3), SI=0(9), , FI=-2(674)}
//[action] 09/04/2010 10:23:35 RomansTest.java TEST FAILED
//[action] 09/04/2010 10:23:35 RomansTest
//[action] 09/04/2010 10:24:35 Romans.java PRODUCTION {MI=0(2), SI=+1(7), , FI=+117(666)}
//[action] 09/04/2010 10:24:38 RomansTest.java TEST OK
//[action] 09/04/2010 10:24:38 RomansTest
//[episode] production 1


public class SensorPlugin implements IStartup {

	public static SensorPlugin plugin;

	public static SensorPlugin getInstance() {
		return plugin;
	}

	private static ActionOutputStream stream;

	public SensorPlugin() {
		super();
		SensorPlugin.plugin = this;

		try {
			stream = new EpisodeClassifierStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void earlyStartup() {

		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new ResourceChangeListener(stream),
				IResourceChangeEvent.POST_CHANGE);
		JavaCore.addElementChangedListener(new JavaStructureChangeListener(
				stream));
		// DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new
		// LaunchListener(stream));
		JUnitCore.addTestRunListener(new JUnitListener(stream));

		WindowListener windowListener = new WindowListener(stream);

		IWorkbench workbench = Activator.getDefault().getWorkbench();
		workbench.addWindowListener(windowListener);

		// makes the installation of the windows' listeners in case we have
		// already an opened document
		windowListener.windowOpened(null);

	}

}

//TODO z rename project. Silver?