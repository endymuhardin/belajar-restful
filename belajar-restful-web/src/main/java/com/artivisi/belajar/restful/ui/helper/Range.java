/**
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artivisi.belajar.restful.ui.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.base.Objects;

public class Range {

	private static final Logger LOGGER = LoggerFactory.getLogger(Range.class);
	private static final Integer DEFAULT_FROM = 0;
	private static final Integer DEFAULT_TO = 19;
	
	private Integer from = DEFAULT_FROM;
	private Integer to = DEFAULT_TO;
	private Long total;
	
	public static Range fromRequestHeader(String strRange){
		Range result = new Range();
		
		if(!StringUtils.hasText(strRange)) {
			return result;
		}
		
		if(!strRange.startsWith("items")) {
			return result;
		}
		
		String[] rangeContent = strRange.split("=");
		if(rangeContent.length != 2) {
			return result;
		}
		
		String[] rangeValue = rangeContent[1].split("-");
		if(rangeValue.length != 2) {
			return result;
		}
		
		try {
			Integer f = Integer.valueOf(rangeValue[0].trim());
			Integer t = Integer.valueOf(rangeValue[1].trim());
			result.setFrom(f);
			result.setTo(t);
		} catch (Exception err){
			LOGGER.info("Invalid range value [{}] - [{}]", rangeValue[0], rangeValue[1]);
			return result;
		}
		
		return result;
	}
	
	public String toResponseHeader(){
		if(from == null || to == null || from < 0 || to < 0) {
			return "";
		}
		String output = "items "+getFrom()+"-"+getTo();
		return total == null ? output : output+"/"+getTotal();
	}
	
	public Range() {
	}
	
	public Range(Integer from, Integer to, Long total) {
		this.from = from;
		this.to = to;
		this.total = total;
		
		if(total != null && total < to) {
			this.to = total.intValue() - 1;
		}
		
	}


	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public Integer getTo() {
		return to;
	}
	public void setTo(Integer to) {
		this.to = to;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hashCode(from, to, total);
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Range){
	        final Range other = (Range) obj;
	        return Objects.equal(from, other.from)
	            && Objects.equal(to, other.to)
	            && Objects.equal(total, other.total);
	    } else{
	        return false;
	    }
	}
	@Override
	public String toString() {
		return "{from:" + from + ", to:" + to + ", total:" + total + "}";
	}
	
	
}
