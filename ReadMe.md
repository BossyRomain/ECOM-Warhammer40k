# ECOM

## Compilation

Pour compiler le projet :

      mvn clean package

## Lancement

### Environnement de développement

Sur Linux:

      java -jar /chemin/vers/le/jar -Dspring.profiles.active=dev -DDEV_FOLDER_PATH=/chemin absolu vers le dossier dev-resources -DANGULAR_ASSETS_PATH=/chemin absolu vers le dossier assets de l'application angular

Sur Windows:

      java -jar /chemin/vers/le/jar --spring.profiles.active=dev --DEV_FOLDER_PATH=/chemin absolu vers le dossier dev-resources --ANGULAR_ASSETS_PATH=/chemin absolu vers le dossier assets de l'application angular

### Environnement de production

Même commande que pour l'environnement de développement mais replacer **dev** par **prod**.
