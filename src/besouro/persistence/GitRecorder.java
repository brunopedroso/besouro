package besouro.persistence;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;


public class GitRecorder {

	private File baseDir;
	private File gitDir;
	private Repository repo;
	private Git git;

	public GitRecorder(File basedir) {
		try {
			
			this.baseDir = basedir;
			gitDir = new File(this.baseDir, ".git");
			
			RepositoryBuilder builder = new RepositoryBuilder();
			builder.setGitDir(gitDir);
			
			repo = builder.build();
			git = new Git(repo);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Repository createNewRepo() {
		
		try {
			
			if (!gitDir.exists()){
				repo.create();
			}
			
			return repo;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addAllFiles() {
		try {
			
			git.add().addFilepattern(".").call();
			
		} catch (NoFilepatternException e) {
			throw new RuntimeException(e);
		}
	}

	public void commit() {
		try {
			
			git.commit()
				.setAll(true)
				.setMessage("besouro automatic message")
				.setCommitter("somename", "someemail")
				.call();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
