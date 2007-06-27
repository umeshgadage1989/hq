import org.hyperic.hq.hqu.rendit.BaseController

class ConsoleController extends BaseController { 
    final HIBERNATE_PREMADE = """
import org.hibernate.criterion.Restrictions

def persistedClass = org.hyperic.hq.appdef.server.session.Platform
def sess  = org.hyperic.hibernate.Util.sessionFactory.currentSession

sess.createCriteria(persistedClass).
  setMaxResults(10).
  list()
"""
    final TEMPLATES = ['Hibernate Query' : HIBERNATE_PREMADE]
        
	def ConsoleController() {
        setTemplate('standard')
        addBeforeFilter({ 
            if (!user.isSuperUser()) {
                render(inline: "Unauthorized")
                return true
            }
            return false
        })
	}
	
    def index(params) {
    	def r = [:]
    
    	if (params.hasOne('code_input')) {
		    r['last_code']   = params.getOne('code_input')
			r['last_result'] = executeCode(r['last_code']) 
		} else {
			r['last_code']   = '1 + 2'
			r['last_result'] = '3'
		}
    	
    	render(action:'index', locals:[r:r, templates:TEMPLATES])
    }
    
    def chooseTemplate(params) {
        def tmpl = TEMPLATES[params.getOne('template')]
        index(['code_input' : [tmpl]])
    }
    
    private def executeCode(code) {
        log.info "Requested to execute code\n${code}\n"
		File tmp = File.createTempFile('gcon', null)
        log.info "Writing tmp file: ${tmp.absolutePath}"
		tmp.withWriter { writer -> 
			writer.write(code)
		}
		
		def eng = new GroovyScriptEngine('.', 
		                                 Thread.currentThread().contextClassLoader)
		def res
		try {
			res = eng.run(tmp.absolutePath, new Binding())
			log.info "Result: [${res}]"
		} catch(Exception e) {
		    log.info "Exception thrown", e
		    def sw = new StringWriter()
		    def pw = new PrintWriter(sw)
		    e.printStackTrace(pw)
		    pw.flush()
		    res = sw.toString()
		}
		res
    }
}
