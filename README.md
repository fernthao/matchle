## CLI wordle-like game in Java
Project submitted under homework 12 in CSDS 293 - Software Craftmanship class

### Features
- Game logic that mimics wordle: prompting the user to guess a word of a certain length, giving feedback on the accuracy of each letter. Three types of matches: perfect match (right letter at right place), partial match (right letter, wrong place), non-match (wrong letter)
- Test cases that cover the full codebase
- Ant build file to specify build and test targets
- Comments and Barricade architecture, adhering to best software craftmanship practices.

### Demo
![Matchle Game Demo](matchle-demo.gif)

### Instructions
1. Clone this repo to your computer
2. Install dependencies
    - Java (openjdk 21.0.7 or later)
    - Ant (https://ant.apache.org/bindownload.cgi)
3. Add both dependencies' binaries to your PATH if not already included
4. **Optional**: Change the word list, word length (number of letters) and max number of attempts by modifying the game initializer inside Game.java.
    - Word list: Your target word is picked from the word list (also called corpus), and words that are not in the list are not accepted as guesses. Each line in the word list file contains one word. You can modify wordlist.txt directly or link to your own .txt word list file.
    - Example: Game with 5 letter words and maximum 6 guesses, loaded from wordlist.txt:
        - `Game game = Game.from("wordlist.txt", 5, 6);`
5. In the terminal, change directory into the cloned repo
6. Run `ant build`
7. Run `ant run`
8. Enter your guess and press Enter. You will see an error message and prompt to enter another guess if you violate the rules:
    - Guesses MUST be among the words listed in wordlist.txt
    - Guesses MUST have the number of letters specified in the game header

### Troubleshooting
- If `ant` command is not found, ensure Ant binary is in your PATH
- If `javac` command is not found, ensure Java JDK binary is in your PATH
- Check your shell manual for adding path. Example: 
    - bash shell: `export PATH="/path/to/your/binary:$PATH"`
    - fish shell: `fish_add_path` 