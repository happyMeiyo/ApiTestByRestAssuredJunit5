pipeline {
	agent any
	stages {
		stage('Preparation') {
			steps {
				//Wait 2 minutes
				sleep(time: 2, unit: 'MINUTES')
				// Get some code from a GitHub repository
				git(url: 'git@*****.git', branch: 'master', changelog: true, credentialsId: '**')
			}
		}

		stage('Test') {
			steps {
				sh 'mvn test'
			}
		}
	}

	post {
		always {
			echo 'allure report'
			allure jdk: 'JDK8', results: [[path: 'target/allure-results']]
		}
	}
}
