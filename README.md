# PhySIM-GUI

A 2D Physics Simulator written in Java. Implements Newton's gravitation laws and constant accelaration simulations. Bodies are loaded from json files (examples in /resources/bodies)


## Usage: 

  simulator.launcher.Main [-cmp <arg>] [-dt <arg>] [-eo <arg>] [-fl <arg>] [-h] [-i <arg>] [-m <arg>] [-o <arg>] [-s <arg>]
 -cmp,--comparator <arg>       State comparator to be used when comparing
                               states. Possible values: 'masseq' (Mass
                               equal states comparator builder), 'epseq'
                               (Epsilon equal states comparator builder).
                               You can provide the 'data' json attaching
                               :{...} to the tag, but without spaces..
                               Default value: 'epseq'. (Ignore, just for debugging)
 -dt,--delta-time <arg>        A double representing actual time, in
                               seconds, per simulation step. Default
                               value: 2500.0.
 -eo,--expected-output <arg>   The expected output file. If not provided
                               no comparison is applied
 -fl,--force-laws <arg>        Force laws to be used in the simulator.
                               Possible values: 'nlug' (Newton’s law of
                               universal gravitation), 'mtcp' (Moving
                               towards a fixed point), 'ng' (No force).
                               You can provide the 'data' json attaching
                               :{...} to the tag, but without spaces..
                               Default value: 'nlug'.
 -h,--help                     Print this message.
 -i,--input <arg>              Bodies JSON input file.
 -m,--mode <arg>               Execution Mode. Possible values: ’batch’
                               (Batch mode), ’gui’ (Graphical User
                               Interface mode). Default value: ’batch’.
 -o,--output <arg>             Output file, where output is written
                               Default value: the standard output.
 -s,--steps <arg>              An integer representing the number of
                               simulation steps. Default value: 150.
