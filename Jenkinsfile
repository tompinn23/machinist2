pipeline {
    agent any

    stages {
        stage('Gradle Setup') {
			steps {
				script {
					sh './gradlew'
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
		stage('Artifacts') {
			steps {
				archiveArtifacts artifacts: 'build/libs/*.jar'
			}
		}
    }
}
