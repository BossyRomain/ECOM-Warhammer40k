# ECOM

## Compilation

Pour compiler le projet :

      mvn clean package

## Lancement

### Environnement de développement

Sur Linux:

      java -Dspring.profiles.active=dev -Ddev.resources=/chemin absolu vers le dossier dev-resources -Dangular.assets=/chemin absolu vers le dossier assets de l'application angular -jar /chemin/vers/le/jar

Sur Windows:

      java --spring.profiles.active=dev --dev.resources=/chemin absolu vers le dossier dev-resources --angular.assets=/chemin absolu vers le dossier assets de l'application angular -jar /chemin/vers/le/jar

### Environnement de production

Même commande que pour l'environnement de développement mais replacer **dev** par **prod**.
