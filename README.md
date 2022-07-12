# GieseCompiler

This contains all parts of a compiler structure around the mini-pascal language. The parts included are Scanner, Recognizer/Parser, Symbol Tree, Syntax Tree, Semantic Analysis, and Code Generation. The program will take in a file from the command line, analyze it to ensure correct pascal grammar, break it into a syntax tree and symbol table, and then generate MIPS assembly code based on the given input.

Extra: Integrated floats into scanner and parser. Tests set up in SemanticTest, and CodeGenTest that show it being matched and used properly, generating syntax tree, and MIPS code with floating point numbers. MIPS code doesn't pass through QtSpim, so it most likely isn't set up right in CodeGeneration.