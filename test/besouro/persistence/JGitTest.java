package besouro.persistence;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JGitTest {

	private File basedir;
	private File gitDir;
	
	private Repository repo;
	private Git git;


	@Before
	public void setup() throws Exception {
		
		basedir = new File("test/basedir");
		Assert.assertFalse(basedir.exists());
		
		basedir.mkdir();
		
		gitDir = new File(this.basedir, ".git");
		
		RepositoryBuilder builder = new RepositoryBuilder();
		builder.setGitDir(gitDir);
		
		repo = builder.build();
		repo.create();
		
		git = new Git(repo);

	}
	
	@After
	public void teardown() {
		deleteFileTree(basedir);
	}

	private static void deleteFileTree(File file) {
		if (file.isDirectory()) {
			for(File f: file.listFiles()) {
				deleteFileTree(f);
			}
		}
		file.delete();
	}

	@Test
	public void shouldCreateAGitRepository() throws Exception {
		
		File[] fileListing = basedir.listFiles();
		Assert.assertEquals(1, fileListing.length);
		Assert.assertEquals(".git", fileListing[0].getName());
		
	}

	
	@Test
	public void shouldAddAllFiles() throws Exception {

		new File(basedir, "aa").createNewFile();
		new File(basedir, "bb").createNewFile();
		new File(basedir, "cc").createNewFile();
		Assert.assertEquals(0, repo.getIndex().getMembers().length);
		
		git.add().addFilepattern(".").call();
		
		Assert.assertEquals(3, repo.getIndex().getMembers().length);
		Assert.assertEquals("aa", repo.getIndex().getMembers()[0].getName());
		Assert.assertEquals("bb", repo.getIndex().getMembers()[1].getName());
		Assert.assertEquals("cc", repo.getIndex().getMembers()[2].getName());
	}


	@Test
	public void shouldCommit() throws Exception {
		
		ObjectId head = repo.resolve("HEAD");
		System.out.println(head);
		
		new File(basedir, "aa").createNewFile();
		
		git.add().addFilepattern(".").call();
		git.commit()
			.setAll(true)
			.setMessage("besouro automatic message")
			.setCommitter("somename", "someemail")
			.call();
		
		head = repo.resolve("HEAD");
		String firstCommit = head.getName();
		
		new File(basedir, "bb").createNewFile();
		
		git.add().addFilepattern(".").call();
		git.commit()
			.setAll(true)
			.setMessage("besouro automatic message")
			.setCommitter("somename", "someemail")
			.call();
		
		head = repo.resolve("HEAD");
		
		RevWalk walk = new RevWalk(repo);
		RevCommit commit = walk.parseCommit(head);
		
		Assert.assertEquals(firstCommit, commit.getParent(0).getName());
		
	}
	
	
}
