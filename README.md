# nativedemo
## for self study.

### generate header files.
* how to generate headers.
```bat
% cd $(project-root)
% javah -cp $(class-path) $(class-name)
```

* example
```bat
% cd nativedemo
% javah -cp build/classes sato.nativedemo.primitive.PrimitiveSample
```
### execute
* execute
```bat
% java -Djava.library.path=./dll/ [class]
```
* example
```bat
% java -Djava.library.path=./dll/ sato.nativedemo.PrimitiveArraySampleMain
```

