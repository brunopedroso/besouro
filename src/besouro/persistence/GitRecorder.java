package besouro.persistence;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import besouro.model.Action;
import besouro.model.EditAction;
import besouro.stream.ActionOutputStream;


public class GitRecorder implements ActionOutputStream {

	private File gitDir;
	private Git git;

	public GitRecorder(File basedir) {
		try {
			
			gitDir = new File(basedir, ".git");
			
			RepositoryBuilder builder = new RepositoryBuilder();
			builder.setGitDir(gitDir);
			
			Repository repo = builder.build();
			git = new Git(repo);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addAction(Action action) {
		if (action instanceof EditAction) {
			addAllAndCommit();
		}
	}
	
	public void close() {
		addAllAndCommit();
	}

	private void addAllAndCommit() {
		try {
			
			git.add().addFilepattern(".").call();
			git.commit()
				.setAll(true)
				.setCommitter("somename", "someemail")
				.setMessage("besouro automatic message")
				.call();
				
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/** Only for testing purposes  */
	public void setGit(Git git) {
		this.git = git;
	}

	public void createRepoIfNeeded() {
		try {
			if (!gitDir.exists()) {
				git.getRepository().create();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
