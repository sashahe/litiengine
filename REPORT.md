# Report for assignment 3

## Project

Name: LITIengine

URL: https://github.com/sashahe/litiengine

The LITIEngine is an open source game engine for developing 2D games in Java.

## Onboarding experience

##### Did it build as documented?

There are no clear guidelines regarding specifically how to build the project, however, there is a file describing how to contribute to the project. Since there is a gradle directory in the project, it can be assumed that gradle is used. Thus, building the project is in fact easy. Aside from downloading Gradle, no additional tools are need to build the software and no additional components were installed when building. The build included compilation as well as testing and no errors occurred during the build of the project, thus the tests are successful. The build was run with the command `gradle build`.

## Complexity

##### 1. What are your results for the ten most complex functions? (If ranking is not easily possible: ten complex functions)?

The following table shows the ten most complex functions that were found by `lizard` and the computed cyclomatic complexity by lizard (CCN1) as well as the cyclomatic complexity counted by hand (CCN2). The formula used for calculating CCN2 was [M = E − N + 2P](https://en.wikipedia.org/wiki/Cyclomatic_complexity).

| ID  | File                                                                           | Function              | CCN1 | CCN2 |
| --- | ------------------------------------------------------------------------------ | --------------------- | ---- | ---- |
| 1   | litiengine/src/de/gurkenlabs/litiengine/configuration/ConfigurationGroup.java  | storeProperties       | 19   | 18   |
| 2   | litiengine/src/de/gurkenlabs/litiengine/environment/Environment.java           | add                   | 18   | 17   |
| 3   | litiengine/src/de/gurkenlabs/litiengine/environment/Environment.java           | remove                | 18   |      |
| 4   | litiengine/src/de/gurkenlabs/litiengine/environment/MapObjectSerializer.java   | getPropertyValue      | 18   | 18   |
| 5   | litiengine/src/de/gurkenlabs/litiengine/environment/tilemap/MapUtilities.java  | getTile               | 18   | 17   |
| 6   | litiengine/src/de/gurkenlabs/litiengine/environment/CustomMapObjectLoader.java | invoke                | 17   |      |
| 7   | litiengine/src/de/gurkenlabs/litiengine/graphics/RenderComponent.java          | render                | 17   |      |
| 8   | litiengine/src/de/gurkenlabs/litiengine/environment/tilemap/xml/Layer.java     | afterUnmarshal        | 16   | 15   |
| 9   | litiengine/src/de/gurkenlabs/litiengine/environment/tilemap/MapUtilities.java  | assessHexStaggering   | 16   | 15   |
| 10  | litiengine/src/de/gurkenlabs/litiengine/util/geom/GeometricUtilities.java      | getIntersectionPoints | 16   | 16   |

###### a. Did all tools/methods get the same result?

The calculations we performed resulted in very similar results. However, something that the calculations had in common, compared to the results from running lizard, was that they were slightly lower in general (see function #1, #2, #5, #8 and #9).

###### b. Are the results clear?

Most functions were relative straightforward which made it easy to graph. As such, it was somewhat clear how lizard calculated the CNN, given that the hand calculated CCN gave a similar result to lizard's CCN.

##### 2. Are the functions just complex, or also long?

The length of the functions is varying, from around 35 lines to 75. In other words, not all complex functions in this project are very long.

##### 3. What is the purpose of the functions?

The purpose of the functions also vary. Some are regarding the environmental setup of the engine, thus they are complex. Others are functions related to utilities, e.g. finding a point that intersects two lines.

##### 4. Are exceptions taken into account in the given measurements?

Yes. Some of the methods examined, e.g. `storeProperties`, handles exceptions. This increased the CC count by one, since it increases the possible paths through the function by one.

##### 5. Is the documentation clear w.r.t. all the possible outcomes?

The documentation regarding the possible paths is varying. The method `getIntersectionPoints` does not explain the possible paths, for example if an intersection point does not exist, it is not defined what the result will be. In general the documentation of the different paths possible and their corresponding results is lacking. Some of the methods examined do not have a method comment, e.g. `afterUnmarshal`.

## Coverage

### Tools

LITIEngine used a software called _SonarCloud_ which has a feature of showing the line and/or condition coverage of a code. Since the LITIEngine community has already integrated a coverage tool to their build environment, it was relatively easy to use it. SonarCloud provides a GUI for the user to access and read the coverage for all and specific code in the project. How to setup/integrate/use [SonarCloud](https://sonarcloud.io/about) is fairly well documented and if neccessary there are also guides in [other websites](https://www.azuredevopslabs.com/labs/vstsextend/sonarcloud/) that provide information on how to do that.

We also made use of _lizard_ which is a tool that calculates the cyclomatic complexity for a given code. The tool was not very well documented but with a few searches we were able to figure out how to install and use lizard. We mostly used it to find the 10 functions with the highest CCN.

### DYI

To see the patch for the implemented branch coverages use the following git command:

```shell
git diff aed8f170c90315faa2b72c6c5894f63cfa6519c6
```

##### What kinds of constructs does your tool support, and how accurate is its output?

To use our ad-hoc tool the user needs to identify and mark down in the source code the possible branches (if, while etc) with a unique number. E.g. `branches[1] = 1`, which means that branch #1 in function A is marked with a 1 if the (current) test cases reaches this branch. Before each given function we create the data structure `branches` to contain the coverage information about specific branches, in which the size of `branches` is assigned by the user with `numberOfBranches`. In addition, we add "else" clauses if none exist. In the case of ternary and exceptions, we count them as an if-statement. At the end of the program, the coverage information is written out to `.csv` files which are handled by `branchCoverage.py` function and a script `testCoverage.sh` that writes out the results to the file `result.txt`.

The tool might not be accurate because the program could contain "dead code" and if flagged, might not allow the tool to reach 100% branch coverage.

### Evaluation (Not done)

Report of old coverage: [Old Results](https://github.com/sashahe/litiengine/blob/testCoverage/branchtest/result.txt)

Report of new coverage: [New Results](https://github.com/sashahe/litiengine/blob/test-branch/branchtest/result.txt)

To see the patch for the added test cases use the following git command:

```shell
git diff <insert commit ID>
```

## Refactoring (Not done)

Function 6 `invoke`:
 The function is responsible both for finding out what constructors an object have, prioritizing them, and invoking the constructor with the highest priority.If finding and/or prioritizing the object's constructors was moved to an external function the code would be simpler to read.  

Function 10 `getTile`: 
	The complexity of this function is very high because it handles different map orientations. To reduce the complexity the latter part of the funciton should be a separate function, dealing with the hexagonal case. This would greatly reduce the complexity of `getTile`, with a branch representing a valid or invalid coordinate for x and y. The new function dealing with the hexagonal map orientation is consice. However, it deals with increased complexity due to the fact that tiles can be located outside the map and these cases create new branches.


# Effort spent (Not done)

For each team member, how much time was spent in

1.  plenary discussions/meetings;

- Adib = 2 hours
- Emelie = 3 hours
- Gustaf = 3 hours
- Sasha = 3 hours
- Vera = 3 hours

2.  discussions within parts of the group;

- Adib = 1.5 hours
- Emelie = 2 hours
- Gustaf = 1.5 hours
- Sasha = 2 hours
- Vera = 2 hours

3.  reading documentation;

- Adib = 3 hours
- Emelie = 2 hours
- Gustaf = 2 hours
- Sasha = 2 hours
- Vera = 2 hours

4.  configuration;

- Adib = 1 hour
- Emelie = 1 hour
- Gustaf = 3 hours
- Sasha = 6 hours
- Vera = 1 hour

5.  analyzing code/output;

- Adib = 3 hours
- Emelie = 2 hours
- Gustaf = 4 hours
- Sasha = 4 hours
- Vera = 4 hours

6.  writing documentation;

- Adib = 1 hour
- Emelie = 4 hours
- Gustaf = 1 hour
- Sasha = 2 hours
- Vera = 1 hour

7.  writing code;

- Adib = 3 hours
- Emelie = 2 hours
- Gustaf = 4 hours
- Sasha = 8 hours
- Vera = 5 hours

8.  running code?

- Adib = 2.5 hours
- Emelie = 2 hours
- Gustaf = 2 hours
- Sasha = 3 hours
- Vera = 2.5 hours

## Overall experience

Working with this project gave us an insight on how it is to work with open source projects. It was also interesting to see how different people decided to implement certain features, how they structured their code as well as learned about new Java frameworks. In addition, we were able to learn new tools such as, _Lizard_ and _SonarCloud_, that we can integrate in future projects. This also allowed us to learn more about coverage which deepened our knowledge on software testing.
