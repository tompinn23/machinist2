pipeline {
    agent any

    stages {
        stage('Gradle Setup') {
			steps {
				script {
					sh './gradlew setupCIWorkspace'
				}
			}
        }
        stage('Gradle Build') {
			steps {
				script {
					sh './gradlew build'
				}
			}
		}
		stage('Artifacts') 
		archiveArtifacts artifacts: 'build/libs/*.jar'
    }
}