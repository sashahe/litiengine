# Report for assignment 4

## Project

Name: LITIEngine

URL: https://github.com/gurkenlabs/litiengine

The LITIEngine is an open source game engine for developing 2D games in Java.

## Selected issue(s)

Title: Generalize Ability system #115

URL: https://github.com/gurkenlabs/litiengine/issues/115

Description: Make the ability class more general, as it is now it requires a creature. This could be more general by not requiring this.

Title: Add Appearance properties for borders #224

URL: https://github.com/gurkenlabs/litiengine/issues/224

Description: Add Appearance properties for borders.

## Onboarding experience

##### Did it build as documented?
There are no clear guidelines regarding specifically how to build the project, however, there is a file describing how to contribute to the project. Since there is a gradle directory in the project, it can be assumed that gradle is used. Thus, building the project is in fact easy. Aside from downloading Gradle, no additional tools are need to build the software and no additional components were installed when building. The build included compilation as well as testing and no errors occurred during the build of the project, thus the tests are successful. The build was run with the command `gradle build`.

## Refactoring plan
The Ability class shall be made parametrized, the constructor of Ablility will take any object whose class is a subclass of CombatEntity. Before the refactoring the constructor would only take objects of class Creature. Thus the Ability class becomes more general and can be applied to anything that is a CombatEntity. The Emitter class will then extend CombatEntity and thus it will be possible for Emitters to have abilities. 

## Requirements affected by functionality being refactored
An ability is initialized with an executor. In the current state this executor must be a creature. The methods within the ability class that involve the executor are:

<table>
  <tr>
    <td>Function</td>
    <td>Requirements on executor: </td>
    <td>Requirements for testing:</td>
  </tr>
  <tr>
    <td>Ability()</td>
    <td>Current implementation requires the executor to be a Creature. This should be refactored to instead require that the executor implements IEntity.</td>
    <td></td>
  </tr>
  <tr>
    <td>calculateImpactArea()</td>
    <td>Implements getAngle()</td>
    <td>Return the Shape given by internalCalculateImpactArea and the angle</td>
  </tr>
  <tr>
    <td>calculatePotentialImpactArea()</td>
    <td>Implements getCollisionBox()</td>
    <td>Return the Ellipse2D describing the potential impact area given by the ability’s range and the executors collision box</td>
  </tr>
  <tr>
    <td>canCast()</td>
    <td>Implements isDead()<br></td>
    <td>Return true when casting is possible for the executor<br><br>Return false when casting is not possible for the executor</td>
  </tr>
  <tr>
    <td>getOrigin()</td>
    <td>Implements getCollisionBox(), getCenter(), getX(), getY(), getLocation()</td>
    <td>Returns executors collisionbox center when the origin of the ability is set to COLLISIONBOX_CENTER<br><br>Returns executors center when the origin of the ability is set to DIMENSION_CENTER<br><br>Returns executors x,y coords incremented with origins x,y coords when the origin of the ability is set to CUSTOM<br>Returns executor’s location on the map either by default or when the origin of the ability is set to LOCATION.<br></td>
  </tr>
  <tr>
    <td>getRemainingCooldownInSeconds()</td>
    <td>Executor is either null or  implements isDead()</td>
    <td>Returns 0 if executor is dead<br><br>Return 0 if executor is null<br><br>Returns 0 if executor can not cast (cooldown is 0 if we can cast)<br><br>Returns remaining cooldown in seconds when there is a remaining cooldown</td>
  </tr>
</table>

## Existing test cases relating to refactored code
There are two test cases that test initialization of an ability and the effect of an ability on a different entity. The actual requirements that are tested are not documented.

## The refactoring carried out
The reactoring effort can be seen by checking out issue/115

`git checkout issue/115`

Before the refactoring the Ability class would only accept objects of type Creature. Creature was a subclass of CombatEntity and CollisionEntity, which in turn were subclasses of Entity. Another direct subclass of Entity was the Emitter class. Since the Ability class only worked with Creatures it was not possible for Emitters to have abilities. Below is a UML diagram showing the relationships between the classes `Entity`, `CombatEntity`, `Emitter`, `CollisionEntity`, `Creature`, and `Ability` as well as their respective functions and local variables before any refactoring took place.

![UML before refactoring](https://github.com/sashahe/litiengine/blob/presentation/BeforeUML.png).

During refactoring the Ability class was made more general by allowing it to work with all subclasses of CombatEntity. Emitters were then made to be a subclass of CombatEntity instead of a direct subclass to Entity. The Creature class specific methods used in the Ability class were moved to the CombatEntity class and then overridden appropriately for the Emitter class. The end result is a more general Ability class which allows for all types of CombatEntites to use abilities. The resulting UML diagram is shown here:
![UML after refactoring](https://github.com/sashahe/litiengine/blob/presentation/AfterUML.png)

## Test logs

Load the index.html file for the [Pre-refactor logs](https://github.com/sashahe/litiengine/blob/presentation/PreRefactorCoverage) in a browser.

Load the index.html file for the [After-refactor logs](https://github.com/sashahe/litiengine/blob/presentation/AfterRefactorCoverage) in a browser.

## Effort spent

For each team member, how much time was spent with the issue(s) can be seen in this [Google Excel Sheet](https://docs.google.com/spreadsheets/d/18gE_6OkY4YIi1d2UZoZI371ZuFIf79GRooNjCFZExkg/edit?usp=sharing).

## Overall experience

Working with this project gave us an insight on how it is to work with open source projects. We experienced how documentation and unit tests of varying quality effect the onboarding effort of contribution to a project. It was also interesting to see how different people had different insigts and thoughts regarding refactoring, how they structured their code as well as learn about new Java frameworks. In addition, we were able to learn new tools such as, ObjectAid for Eclipse for UML diagram generation, that we can integrate in future projects. This also allowed us to learn more about refactoring which deepened our knowledge on software development.
