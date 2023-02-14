package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.EpsilonEqualStatesBuilder;
import simulator.factories.Factory;
import simulator.factories.MassEqualStatesBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.MovingTowardsFixedPointBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoForceBuilder;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Integer _stepsDefaultValue = 150;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static Integer _steps = null;
	private static String _mode = null;
	private static String _inFile = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;
	private static String _outFile = null;
	private static String _expectedOutputFile = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {
		// TODO initialize the bodies factory
		
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		// TODO initialize the force laws factory
		ArrayList<Builder<ForceLaws>> flBuilders = new ArrayList<>();
		flBuilders.add(new NewtonUniversalGravitationBuilder());
		flBuilders.add(new MovingTowardsFixedPointBuilder());
		flBuilders.add(new NoForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(flBuilders);
		// TODO initialize the state comparator
		ArrayList<Builder<StateComparator>> scBuilders = new ArrayList<>();
		scBuilders.add(new MassEqualStatesBuilder());
		scBuilders.add(new EpsilonEqualStatesBuilder());
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(scBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);

			//support of -o, -eo, and -s (define corresponding parse methods)
			parseOutputOption(line);
			parseExpectedOutputOption(line);
			parseStepsOption(line);
			parseMode(line);
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// cmdLineOptions)
		
		// expected-output
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg()
				.desc("The expected output file. If not provided no comparison is applied")
				.build());
		
		// steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg()
				.desc("An integer representing the number of simulation steps. Default value: " 
						+ _stepsDefaultValue.toString() + ".")
				.build());

		// output
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Output file, where output is written Default value: the standard output.")
				.build());


		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());
		
		
		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg()
				.desc("Execution Mode. Possible values: ’batch’ (Batch mode), ’gui’ (Graphical User Interface mode). Default value: ’batch’.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	private static void parseMode(CommandLine line) {
		_mode = line.getOptionValue("m");
		if (_mode == null) {
			_mode = "batch";
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
	}
	
	
	private static void parseOutputOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		if (line.getOptionValue("s") == null) 
			_steps = _stepsDefaultValue;
		else
			_steps = Integer.parseInt(line.getOptionValue("s"));
	}
	
	
	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}
	
	
	
	
	
	
	private static void parseExpectedOutputOption(CommandLine line) throws ParseException {
		_expectedOutputFile = line.getOptionValue("eo");
	}
	
	

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() throws Exception {
		//create forcelaws and simulator
		ForceLaws fl = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhysicsSimulator simulator = new PhysicsSimulator(_dtime, fl);
		//prepare io files
		InputStream expectedOutput = null, inputBodies = null;
		OutputStream outStream = null;
		if (_inFile == null) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
		if(_expectedOutputFile != null)
			try {
				expectedOutput = new FileInputStream(_expectedOutputFile);
			}catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } 
		try {
			inputBodies = new FileInputStream(_inFile);
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		if (_outFile == null) {
			outStream = System.out;
		} else {
			outStream = new FileOutputStream(new File(_outFile));
		}
		
		//create state comparator and controller
		StateComparator sc = _stateComparatorFactory.createInstance(_stateComparatorInfo);
		Controller controller = new Controller(simulator, _bodyFactory, _forceLawsFactory);
		
		//load bodies
		controller.loadBodies(inputBodies);
		inputBodies.close();
		
		//run simulation
		controller.run(_steps, outStream, expectedOutput, sc);
		
		//close remaining streams;
		if(_expectedOutputFile != null) 
			expectedOutput.close();
		outStream.close();
		
	}
	
	private static void startGUIMode() throws Exception {
		//create forcelaws and simulator
		ForceLaws fl = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhysicsSimulator simulator = new PhysicsSimulator(_dtime, fl);
		//prepare io files
		InputStream inputBodies = null;
		if(_inFile != null)
			inputBodies = new FileInputStream(_inFile);
		
		Controller controller = new Controller(simulator, _bodyFactory, _forceLawsFactory);
		
		//load bodies
		if (_inFile != null) {
			controller.loadBodies(inputBodies);
			inputBodies.close();
		}
		
		//create main window
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
			new MainWindow(controller);
			}
			});
		
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if(_mode.equals("gui"))
			startGUIMode();
		else
			startBatchMode();
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
