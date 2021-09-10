pipeline {
    agent { label 'docker' }
    stages {
        stage('Build') {
            steps {
                withDockerRegistry([credentialsId: 'fintlabsacr.azurecr.io', url: 'https://fintlabsacr.azurecr.io']) {
                    sh "docker pull fintlabsacr.azurecr.io/vigo-auth-organisation-selector-frontend:latest"
                    sh "docker build --tag ${GIT_COMMIT} ."
                }
            }
        }
        stage('Publish') {
            when { branch 'main' }
            steps {
                withDockerRegistry([credentialsId: 'fintlabsacr.azurecr.io', url: 'https://fintlabsacr.azurecr.io']) {
                    sh "docker tag ${GIT_COMMIT} fintlabsacr.azurecr.io/vigo-auth-organisation-selector:build.${BUILD_NUMBER}_${GIT_COMMIT}"
                    sh "docker push fintlabsacr.azurecr.io/vigo-auth-organisation-selector:build.${BUILD_NUMBER}_${GIT_COMMIT}"
                }
            }
        }
    }
}