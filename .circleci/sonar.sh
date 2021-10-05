#!/bin/bash -e
function run_sonar() {
    if [ -z "${NO_SONAR}" ]; then

        # Allow overriding default path to mvn
        MVN="${MVN:-./mvnw}"

        # Setup
        if [ -n "${CI_PULL_REQUEST}" ]; then
            export PR_NUMBER=${CI_PULL_REQUEST##*/}
            echo "Configuring sonar for PR $PR_NUMBER"
            export SONAR_OPTS="-Dsonar.pullrequest.key=${PR_NUMBER} -Dsonar.pullrequest.branch=${CIRCLE_BRANCH} -Dsonar.pullrequest.base=main"
        elif [ "$CIRCLE_BRANCH" == "main" ]; then
            unset SONAR_GITHUB_TOKEN
            export SONAR_OPTS=""
        else
            echo "Not on a PR nor on main, I am not doing anything"
            exit 0
        fi

        # Run
        echo "Running sonar"
        $MVN sonar:sonar ${SONAR_OPTS}
    else
        echo "Skipping sonar as requested"
    fi
}

run_sonar
