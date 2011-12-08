package br.com.caelum.revolution.tools.jdepend;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.hibernate.Session;

import br.com.caelum.revolution.builds.BuildResult;
import br.com.caelum.revolution.domain.Commit;
import br.com.caelum.revolution.executor.CommandExecutor;
import br.com.caelum.revolution.persistence.ToolThatPersists;
import br.com.caelum.revolution.scm.GoToChangeSet;
import br.com.caelum.revolution.tools.Tool;
import br.com.caelum.revolution.tools.ToolException;

@GoToChangeSet
public class JDependTool implements Tool, ToolThatPersists {

	private final CommandExecutor executor;
	private final JDependXMLInterpreter interpreter;
	private final String jDependPath;
	private Session session;

	public JDependTool(CommandExecutor executor, JDependXMLInterpreter interpreter, String jDependPath) {
		this.executor = executor;
		this.interpreter = interpreter;
		this.jDependPath = jDependPath;	
	}

	public void calculate(Commit commit, BuildResult current) throws ToolException {
		try {
			executor.setEnvironmentVar("CLASSPATH", jDependPath);
			String output = executor.execute("java jdepend.xmlui.JDepend " + current.getDirectory(), current.getDirectory());
			
			String response = extractJDependXMLBlockFrom(output);
			
			List<JDependMetric> infos = interpreter.interpret(new ByteArrayInputStream(response.getBytes("UTF-8")));
			for(JDependMetric info : infos) {
				session.save(info);
			}
		} catch (Exception e) {
			throw new ToolException(e);
		}
	}

	public String getName() {
		return "jdepend";
	}
	
	private String extractJDependXMLBlockFrom(String output) {
		return output.substring(0, output.lastIndexOf("</JDepend>")+10);
	}

	public Class<?>[] classesToPersist() {
		return new Class<?>[] { JDependMetric.class };
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
